/******************************************************************************
 * Copyright 2014-2014 Ricardo F. Guimarães                                    *
 *                                                                             *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may     *
 * not use this file except in compliance with the License. You may obtain a   *
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0           *
 *                                                                             *
 * Unless required by applicable law or agreed to in writing, software         *
 * distributed under the License is distributed on an "AS IS" BASIS,           *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    *
 * See the License for the specific language governing permissions and         *
 * limitations under the License.                                              *
 *                                                                             *
 * This file incorporates work covered by the following copyright and          *
 * permission notice:                                                          *
/******************************************************************************
* Copyright 2013-2014 LASIGE                                                  *
*                                                                             *
* Licensed under the Apache License, Version 2.0 (the "License"); you may     *
* not use this file except in compliance with the License. You may obtain a   *
* copy of the License at http://www.apache.org/licenses/LICENSE-2.0           *
*                                                                             *
* Unless required by applicable law or agreed to in writing, software         *
* distributed under the License is distributed on an "AS IS" BASIS,           *
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    *
* See the License for the specific language governing permissions and         *
* limitations under the License.                                              *
*                                                                             *
*******************************************************************************
* Lists the Matching Algorithms available for the user interface.             *
*                                                                             *
* @author Daniel Faria & Ricardo F. Guimarães                                 *
* @date 07-10-2014                                                            *
* @version 2.02e                                                              *
******************************************************************************/
package aml.settings;

public enum MatchingAlgorithm
{
    AML ("AML Matcher"),
    OAEI ("OAEI2013 Matcher"),
    LEXICAL ("Lexical Matcher"),
    EXTRAS4AML ("Extras4AML Matcher"), //Added by Ricardo F. Guimarães
    AMLEXTRA ("AML + EXTRAS4AML Matcher"), //Added by Ricardo F. Guimarães
    OAEIEXTRA ("OAEI2013 + EXTRAS4AML Matcher"); //Added by Ricardo F. Guimarães
    
    String label;
    
    MatchingAlgorithm(String s)
    {
    	label = s;
    }
    
    public String toString()
    {
    	return label;
    }
	    
	public static MatchingAlgorithm parseMatcher(String matcher)
	{
		for(MatchingAlgorithm m : MatchingAlgorithm.values())
			if(matcher.equals(m.toString()))
				return m;
		return AML;
	}
}
